/**
 * Copyright 2010 ZTEsoft Inc. All Rights Reserved.
 *
 * This software is the proprietary information of ZTEsoft Inc.
 * Use is subject to license terms.
 * 
 * $Tracker List
 * 
 * $TaskId: $ $Date: 9:24:36 AM (May 9, 2008) $comments: create 
 * $TaskId: $ $Date: 3:56:36 PM (SEP 13, 2010) $comments: upgrade jvm to jvm1.5 
 *  
 *  
 */
package exception;

public class BaseAppExceptionDto
{
    private String errorCode;
    private String errorMessage;
    public String getErrorCode()
    {
        return errorCode;
    }
    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }
    public String getErrorMessage()
    {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
}
