package io.github.csgroup.quizmaker.utils;

import io.github.csgroup.quizmaker.data.quiz.QuizMetadata;
import io.github.csgroup.quizmaker.word.TemplateReplacements;

/**
 * Helper class to store the last metadata and replacements that were used so that
 * they stay persistent throughout the program's up-time
 * 
 * @author Samuel Garcia
 */
public class SessionMemory {
    private static final SessionMemory instance = new SessionMemory();

    private QuizMetadata lastMetadata = null;
    private TemplateReplacements lastReplacements = null;

    private SessionMemory() {}

    public static SessionMemory getInstance() {
        return instance;
    }

    public QuizMetadata getLastMetadata() {
        return lastMetadata;
    }

    public void setLastMetadata(QuizMetadata metadata) {
        this.lastMetadata = metadata;
    }

    public TemplateReplacements getLastReplacements() {
        return lastReplacements;
    }

    public void setLastReplacements(TemplateReplacements replacements) {
        this.lastReplacements = replacements;
    }
}

