package log;

public class LogEntry
{
    private final LogLevel m_logLevel;
    private final String m_strMessage;
    
    public LogEntry(LogLevel logLevel, String strMessage)
    {
        m_logLevel = logLevel;
        m_strMessage = strMessage;
    }
    
    public String getMessage()
    {
        return m_strMessage;
    }
    
    public LogLevel getLevel()
    {
        return m_logLevel;
    }
}

