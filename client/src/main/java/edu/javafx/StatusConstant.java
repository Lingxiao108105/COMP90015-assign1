package edu.javafx;

import edu.common.enums.Status;
import javafx.scene.paint.Color;

public class StatusConstant {

    public static final Color OK = Color.GREEN;
    public static final Color WAITING = Color.BLUE;
    public static final Color WARNING = Color.ORANGE;
    public static final Color ERROR = Color.RED;


    public static final String QUERY = "Querying";
    public static final String ADD = "Adding";
    public static final String REMOVE = "Removing";
    public static final String UPDATE = "Updating";

    public static final String EMPTY_OR_BLANK_INPUT = "Search word cannot be blank or empty!";
    public static final String BLANK_POS_ROW = "part of speech cannot be blank or empty!";
    public static final String BLANK_MEANING_ROW = "part of speech cannot be blank or empty!";
    public static final String DUPLICATED_POS = "Same part of speech already exists!";
    public static final String EMPTY_MEANING_TABLE = "Please add some meanings to this word!";

    public static Color colorByStatus(Status status){
        if(status.equals(Status.DUPLICATE)){
            return WARNING;
        }
        if(status.equals(Status.INVALID_FORMAT)){
            return ERROR;
        }
        if(status.equals(Status.INVALID_REQUEST_TYPE)){
            return ERROR;
        }
        if(status.equals(Status.SUCCESS)){
            return OK;
        }
        if(status.equals(Status.NOTFOUND)){
            return WARNING;
        }
        if(status.equals(Status.EMPTY_MEANING)){
            return WARNING;
        }
        return ERROR;
    }

}
