package org.stepdef;

import lombok.Getter;

import java.util.List;

@Getter
class Response
{
    private int page;
    private int perPage;
    private int total;
    private int totalPages;
    private List<Data> data = null;
    private Support support;
}