package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.dto.AuthorWithNewsResponse;
import com.mjc.school.service.query.AuthorServiceSearchParams;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

public interface AuthorController extends BaseController<AuthorDtoRequest, AuthorDtoResponse, Long, JsonPatch, AuthorServiceSearchParams> {

    PagedModel<EntityModel<AuthorWithNewsResponse>> getAuthorsWithNewsAmount(Pageable pageable);
}
