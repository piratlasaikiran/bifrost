package org.bhavani.constructions.helpers;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)
public class PageRequest {

    private Integer pageNumber;
    private int pageSize = 10;
    private Integer offset;

    public int getOffset() {
        if (offset == null) {
            if (pageNumber == null) {
                pageNumber = 0;
            }
            offset = pageNumber * pageSize;
        }
        return offset;
    }

}
