package dtu.thebestprice.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class PageCustom {
    private List content;
    private int number = 0;
    private int totalPages = 0;
    private int totalElements = 0;
    private int size = 0;
    private boolean first;
    private boolean last;
    private boolean empty;
    private int numberOfElements;
}
