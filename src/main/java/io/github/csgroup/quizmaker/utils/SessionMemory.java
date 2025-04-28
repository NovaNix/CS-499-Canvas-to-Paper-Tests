package io.github.csgroup.quizmaker.utils;

import io.github.csgroup.quizmaker.data.quiz.QuizMetadata;
import io.github.csgroup.quizmaker.word.TemplateReplacements;

/**
 * Helper class to store values that were used so that
 * they stay persistent throughout the program's up-time
 * 
 * @author Samuel Garcia
 */
import java.nio.file.Path;

public class SessionMemory
{
    private static final SessionMemory instance = new SessionMemory();
    
    private QuizMetadata lastMetadata;
    private TemplateReplacements lastReplacements;
    
    private Path lastExportQtiFolder;
    private Path lastImportQtiFolder;
    private Path lastExportWordFolder;
    
    private SessionMemory() {}
    
    public static SessionMemory getInstance()
    {
        return instance;
    }
    
    public QuizMetadata getLastMetadata()
    {
        return lastMetadata;
    }

    public void setLastMetadata(QuizMetadata lastMetadata)
    {
        this.lastMetadata = lastMetadata;
    }
    
    public TemplateReplacements getLastReplacements()
    {
        return lastReplacements;
    }

    public void setLastReplacements(TemplateReplacements lastReplacements)
    {
        this.lastReplacements = lastReplacements;
    }
    
    public Path getLastExportQtiFolder()
    {
        return lastExportQtiFolder;
    }

    public void setLastExportQtiFolder(Path path)
    {
        this.lastExportQtiFolder = path;
    }

    public Path getLastImportQtiFolder()
    {
        return lastImportQtiFolder;
    }

    public void setLastImportQtiFolder(Path path)
    {
        this.lastImportQtiFolder = path;
    }

    public Path getLastExportWordFolder()
    {
        return lastExportWordFolder;
    }

    public void setLastExportWordFolder(Path path)
    {
        this.lastExportWordFolder = path;
    }
}


