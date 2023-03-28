package com.mjc.school.hateoas;

import com.mjc.school.controller.impl.AuthorRestController;
import com.mjc.school.controller.impl.CommentRestController;
import com.mjc.school.controller.impl.NewsRestController;
import com.mjc.school.controller.impl.TagRestController;
import com.mjc.school.service.dto.*;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class LinkHelper {

    public static void addLinksToComment(EntityModel<CommentDtoResponse> commentModel) {
        commentModel.add(linkTo(methodOn(CommentRestController.class).getById(commentModel.getContent().id())).withSelfRel());
        commentModel.add(linkTo(methodOn(NewsRestController.class).getById(commentModel.getContent().newsId())).withRel("news"));
    }

    public static void addLinksToAuthor(EntityModel<AuthorDtoResponse> authorModel) {
        authorModel.add(linkTo(methodOn(AuthorRestController.class).getById(authorModel.getContent().id())).withSelfRel());
    }

    public static void addLinksToAuthorWithNewsAmount(EntityModel<AuthorWithNewsResponse> authorModel) {
        authorModel.add(linkTo(methodOn(AuthorRestController.class).getById(authorModel.getContent().id())).withSelfRel());
    }

    public static void addLinksToTag(EntityModel<TagDtoResponse> tagModel) {
        tagModel.add(linkTo(methodOn(TagRestController.class).getById(tagModel.getContent().id())).withSelfRel());
    }

    public static void addLinksToNews(EntityModel<NewsDtoResponse> newsModel) {
        newsModel.add(linkTo(methodOn(NewsRestController.class).getById(newsModel.getContent().id())).withSelfRel());
        newsModel.add(linkTo(methodOn(NewsRestController.class).getAuthorByNewsId(newsModel.getContent().id())).withRel("author"));
        newsModel.add(linkTo(methodOn(NewsRestController.class).getTagsByNewsId(newsModel.getContent().id())).withRel("tags"));
        newsModel.add(linkTo(methodOn(NewsRestController.class).getCommentsByNewsId(newsModel.getContent().id())).withRel("comments"));
    }
}
