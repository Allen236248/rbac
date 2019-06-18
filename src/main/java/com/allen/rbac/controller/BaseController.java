package com.allen.rbac.controller;

import com.allen.rbac.dto.req.PaginationRequestDto;
import com.allen.rbac.util.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    public PageInfo buildPageInfo(PaginationRequestDto paginationRequestDto) {
        Integer pageSize = paginationRequestDto.getPageSize();
        pageSize = (pageSize == null || pageSize < 1) ? PageInfo.DEFAULT_PAGE_SIZE : pageSize;

        Integer pageNumber = paginationRequestDto.getPageNumber();
        pageNumber = (pageNumber == null || pageNumber < 1) ? PageInfo.DEFAULT_CURRENT_PAGE : pageNumber;

        return PageInfo.build(pageSize, pageNumber);
    }

}
