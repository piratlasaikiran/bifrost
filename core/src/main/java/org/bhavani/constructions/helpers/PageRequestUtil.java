package org.bhavani.constructions.helpers;

import static java.util.Objects.isNull;

public class PageRequestUtil {

    private static final int MAX_PAGE_SIZE = 10000;

    public static PageRequest getDefaultPageRequest() {
        return PageRequest.builder().pageSize(MAX_PAGE_SIZE).build();
    }

    public static PageRequest getLimitedPageRequest(Integer limit, Integer offset) {
        if (isNull(limit) || isNull(offset)) {
            return getDefaultPageRequest();
        }
        return PageRequest.builder().pageSize(limit).offset(offset).build();
    }

}
