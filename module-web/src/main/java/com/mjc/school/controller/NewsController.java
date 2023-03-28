package com.mjc.school.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.service.dto.*;
import com.mjc.school.service.query.NewsServiceSearchParams;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

public interface NewsController extends BaseController<NewsDtoRequest, NewsDtoResponse, Long, JsonPatch, NewsServiceSearchParams> {

    CollectionModel<EntityModel<TagDtoResponse>> getTagsByNewsId(Long id);

    CollectionModel<EntityModel<CommentDtoResponse>> getCommentsByNewsId(Long id);

    EntityModel<AuthorDtoResponse> getAuthorByNewsId(Long id);
}

