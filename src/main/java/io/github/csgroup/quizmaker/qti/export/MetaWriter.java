package io.github.csgroup.quizmaker.qti.export;

import io.github.csgroup.quizmaker.data.Quiz;
import org.w3c.dom.Document;

import io.github.csgroup.quizmaker.data.quiz.QuizMetadata;
import io.github.csgroup.quizmaker.qti.export.utils.QTIFileWriter;
import java.util.UUID;
import org.w3c.dom.Element;

/**
 * Converts a {@link Quiz} into a QTI assessment_meta.xml file
 * 
 * @author Michael Nix, Sarah Singhirunnusorn
 */
public class MetaWriter extends QTIFileWriter
{
	private Quiz quiz;
	
	public MetaWriter(Quiz q)
	{
		super();
		
		this.quiz = q;
		
		writeMeta(getDocument(), quiz);
	}
	
	/**
	 * Writes the metadata file to the document
	 * @param d the document to write to
	 * @param q the quiz to write the metadata of
	 * @author Sarah Singhirunnusorn
	 */
	private void writeMeta(Document d, Quiz q)
	{
		// Root element = <quiz> (with identifier, namespaces, and schema location)
		Element quiz = d.createElement("quiz");
		quiz.setAttribute("identifier", q.getId());
		quiz.setAttribute("xmlns", "http://canvas.instructure.com/xsd/cccv1p0");
		quiz.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		quiz.setAttribute("xsi:schemaLocation", "http://canvas.instructure.com/xsd/cccv1p0 https://canvas.instructure.com/xsd/cccv1p0.xsd");
		d.appendChild(quiz);

		// <title>: Quiz title as shown in Canvas
		Element title = d.createElement("title");
		title.setTextContent(q.getTitle());
		quiz.appendChild(title);

		// <description>: Quiz description (HTML)
		Element description = d.createElement("description");
		if (q.getDescription() != null && q.getDescription().asText() != null)
		{
			description.setTextContent(q.getDescription().asText());
		}
		quiz.appendChild(description);

		// <shuffle_answers>: Determines if answer choices are shuffled
		Element shuffle = d.createElement("shuffle_answers");
		shuffle.setTextContent(String.valueOf(q.shouldShuffleAnswers()));
		quiz.appendChild(shuffle);

		// <scoring_policy>: Always "keep_highest" by default
		Element scoring = d.createElement("scoring_policy");
		scoring.setTextContent("keep_highest");
		quiz.appendChild(scoring);

		// <hide_results>: Empty to match Canvas default structure
		quiz.appendChild(d.createElement("hide_results"));

		// <quiz_type>: "Assignment" to match Canvas default structure
		Element quizType = d.createElement("quiz_type");
		quizType.setTextContent("assignment");
		quiz.appendChild(quizType);

		// <points_possible>: Total quiz points
		Element points = d.createElement("points_possible");
		points.setTextContent(String.valueOf(q.getMetadata().getValue(QuizMetadata.MetadataType.Points)));
		quiz.appendChild(points);

		// Lockdown Browser Fields (default to false/empty)
		appendBoolean(d, quiz, "require_lockdown_browser", false);
		appendBoolean(d, quiz, "require_lockdown_browser_for_results", false);
		appendBoolean(d, quiz, "require_lockdown_browser_monitor", false);
		quiz.appendChild(d.createElement("lockdown_browser_monitor_data"));

		// <show_correct_answers>: Correct answers shown after submission
		appendBoolean(d, quiz, "show_correct_answers", true);

		// <anonymous_submissions>: Whether students submit anonymously (default = false)
		appendBoolean(d, quiz, "anonymous_submissions", false);

		// <could_be_locked>: Canvas-specific locking flag
		appendBoolean(d, quiz, "could_be_locked", false);
		
		// <time_limit>: Included only if defined in metadata
		String minutesStr = q.getMetadata().getValue(QuizMetadata.MetadataType.Minutes);
		if (minutesStr != null && !minutesStr.isBlank())
		{
			Element timeLimit = d.createElement("time_limit");
			timeLimit.setTextContent(minutesStr.trim());
			quiz.appendChild(timeLimit);
		}

		// <disable_timer_autosubmission>: Timer behavior when time expires
		appendBoolean(d, quiz, "disable_timer_autosubmission", false);

		// <allowed_attempts>: Number of allowed attempts (default = 1)
		Element allowed = d.createElement("allowed_attempts");
		allowed.setTextContent("1");
		quiz.appendChild(allowed);

		// Quiz Navigation and Availibility Fields
		appendBoolean(d, quiz, "one_question_at_a_time", false);
		appendBoolean(d, quiz, "cant_go_back", false);
		appendBoolean(d, quiz, "available", false);
		appendBoolean(d, quiz, "one_time_results", false);
		appendBoolean(d, quiz, "show_correct_answers_last_attempt", false);
		appendBoolean(d, quiz, "only_visible_to_overrides", false);
		appendBoolean(d, quiz, "module_locked", false);
		
		// <assignment> block: Wraps assignment metadata tied to the quiz
		Element assignment = d.createElement("assignment");
		
		// <assignment identifier> : randomly generated 
		assignment.setAttribute("identifier", "g" + UUID.randomUUID().toString().replace("-", ""));

		// <title>: Assignment title (same as quiz title)
		Element assignTitle = d.createElement("title");
		assignTitle.setTextContent(q.getTitle());
		assignment.appendChild(assignTitle);

		// Due Date Settings (default = empty)
		assignment.appendChild(d.createElement("due_at"));
		assignment.appendChild(d.createElement("lock_at"));
		assignment.appendChild(d.createElement("unlock_at"));

		// <module_locked> 
		appendBoolean(d, assignment, "module_locked", false);

		// <workflow_state>: Published/unpublished status (default = unpublished)
		Element workflow = d.createElement("workflow_state");
		workflow.setTextContent("unpublished");
		assignment.appendChild(workflow);

		// <assignment_overrides>
		assignment.appendChild(d.createElement("assignment_overrides"));

		// <quiz_identifierref>: Links assignment to the corresponding quiz
		Element quizRef = d.createElement("quiz_identifierref");
		quizRef.setTextContent(q.getId());
		assignment.appendChild(quizRef);

		// Additional LMS fields
		assignment.appendChild(d.createElement("allowed_extensions"));
		appendBoolean(d, assignment, "has_group_category", false);

		// Grading & Submission Configuration
		Element assignPoints = d.createElement("points_possible");
		assignPoints.setTextContent(String.valueOf(q.getMetadata().getValue(QuizMetadata.MetadataType.Points)));
		assignment.appendChild(assignPoints);

		Element grading = d.createElement("grading_type");
		grading.setTextContent("points");
		assignment.appendChild(grading);

		appendBoolean(d, assignment, "all_day", false);

		Element submission = d.createElement("submission_types");
		submission.setTextContent("online_quiz");
		assignment.appendChild(submission);

		assignment.appendChild(d.createElement("position")).setTextContent("1");

		// Review & Peer Grading Fields
		appendBoolean(d, assignment, "turnitin_enabled", false);
		appendBoolean(d, assignment, "vericite_enabled", false);
		assignment.appendChild(d.createElement("peer_review_count")).setTextContent("0");
		appendBoolean(d, assignment, "peer_reviews", false);
		appendBoolean(d, assignment, "automatic_peer_reviews", false);
		appendBoolean(d, assignment, "anonymous_peer_reviews", false);
		appendBoolean(d, assignment, "grade_group_students_individually", false);
		appendBoolean(d, assignment, "freeze_on_copy", false);
		appendBoolean(d, assignment, "omit_from_final_grade", false);
		appendBoolean(d, assignment, "hide_in_gradebook", false);
		appendBoolean(d, assignment, "intra_group_peer_reviews", false);
		appendBoolean(d, assignment, "only_visible_to_overrides", false);
		appendBoolean(d, assignment, "post_to_sis", false);
		appendBoolean(d, assignment, "moderated_grading", false);
		
		// Grader Visibility Fields
		assignment.appendChild(d.createElement("grader_count")).setTextContent("0");
		appendBoolean(d, assignment, "grader_comments_visible_to_graders", true);
		appendBoolean(d, assignment, "anonymous_grading", false);
		appendBoolean(d, assignment, "graders_anonymous_to_graders", false);
		appendBoolean(d, assignment, "grader_names_visible_to_final_grader", true);
		appendBoolean(d, assignment, "anonymous_instructor_annotations", false);

		// <post_policy>: Determines when grades are visible to students
		Element postPolicy = d.createElement("post_policy");
		Element postManually = d.createElement("post_manually");
		postManually.setTextContent("false");
		postPolicy.appendChild(postManually);
		assignment.appendChild(postPolicy);

		quiz.appendChild(assignment);
		
		// <assignment_group_identifierref>
		Element groupRef = d.createElement("assignment_group_identifierref");
		groupRef.setTextContent("g" + UUID.randomUUID().toString().replace("-", ""));
		quiz.appendChild(groupRef);

		// Final <assignment_overrides> block (outside assignment)
		quiz.appendChild(d.createElement("assignment_overrides"));
	}
	
	/**
	 * Utility method to append a boolean field as a child element with text content.
	 * @param d the document to write to
	 * @param parent the parent element
	 * @param tag the name of the boolean tag
	 * @param value the true/false value to write
	 */
	private void appendBoolean(Document d, Element parent, String tag, boolean value)
	{
		Element e = d.createElement(tag);
		e.setTextContent(Boolean.toString(value));
		parent.appendChild(e);
	}

}
