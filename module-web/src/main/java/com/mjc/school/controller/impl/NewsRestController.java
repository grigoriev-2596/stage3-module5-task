package com.mjc.school.controller.impl;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.NewsController;
import com.mjc.school.hateoas.LinkHelper;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.CommentService;
import com.mjc.school.service.TagService;
import com.mjc.school.service.dto.*;
import com.mjc.school.service.query.NewsServiceSearchParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mjc.school.constant.ApiConstant.*;

@RestController
@RequestMapping(value = API_BASE_URI, produces = MediaTypes.HAL_JSON_VALUE)
public class NewsRestController implements NewsController {

    private final BaseService<NewsDtoRequest, NewsDtoResponse, Long, JsonPatch, NewsServiceSearchParams> newsService;

    private final TagService tagService;
    private final AuthorService authorService;
    private final CommentService commentService;

    private final PagedResourcesAssembler<NewsDtoResponse> pageAssembler;

    @Autowired
    public NewsRestController(BaseService<NewsDtoRequest, NewsDtoResponse, Long, JsonPatch, NewsServiceSearchParams> newsService,
                              TagService tagService, AuthorService authorService, CommentService commentService,
                              PagedResourcesAssembler<NewsDtoResponse> pageAssembler) {
        this.newsService = newsService;
        this.tagService = tagService;
        this.authorService = authorService;
        this.commentService = commentService;
        this.pageAssembler = pageAssembler;
    }

    @Operation(summary = "Get all news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received all news"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v1" + NEWS_BASE_URI)
    @Override
    public PagedModel<EntityModel<NewsDtoResponse>> getAll(
            @PageableDefault(size = 5)
            @SortDefault(sort = "creationDate", direction = Sort.Direction.DESC)
            Pageable pageable,
            NewsServiceSearchParams params) {

        PagedModel<EntityModel<NewsDtoResponse>> modelPage = pageAssembler.toModel(newsService.getAll(pageable, params));
        modelPage.forEach(LinkHelper::addLinksToNews);
        return modelPage;
    }

    @Operation(summary = "Get news by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received a news by its id"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/v1" + NEWS_BASE_URI + "/{id:\\d+}")
    @Override
    public EntityModel<NewsDtoResponse> getById(@PathVariable Long id) {

        EntityModel<NewsDtoResponse> model = EntityModel.of(newsService.getById(id));
        LinkHelper.addLinksToNews(model);
        return model;
    }

    @Operation(summary = "Create a news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "News was created successfully"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/v1" + NEWS_BASE_URI, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public EntityModel<NewsDtoResponse> create(@RequestBody NewsDtoRequest createRequest) {

        EntityModel<NewsDtoResponse> model = EntityModel.of(newsService.create(createRequest));
        LinkHelper.addLinksToNews(model);
        return model;
    }

    @Operation(summary = "Update a news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "News was updated successfully"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/v1" + NEWS_BASE_URI + "/{id:\\d+}", consumes = "application/json-patch+json")
    @Override
    public EntityModel<NewsDtoResponse> update(@PathVariable Long id, @RequestBody JsonPatch patch) {

        EntityModel<NewsDtoResponse> model = EntityModel.of(newsService.update(id, patch));
        LinkHelper.addLinksToNews(model);
        return model;
    }

    @Operation(summary = "Delete news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "News was deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/v1" + NEWS_BASE_URI + "/{id:\\d+}")
    @Override
    public void deleteById(@PathVariable Long id) {
        newsService.deleteById(id);
    }


    @Operation(summary = "Get tags by news id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received tags by news id"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/v1" + NEWS_BASE_URI + "/{id:\\d+}" + TAGS_BASE_URI)
    @Override
    public CollectionModel<EntityModel<TagDtoResponse>> getTagsByNewsId(@PathVariable Long id) {

        List<EntityModel<TagDtoResponse>> tagModels = tagService.getByNewsId(id).stream().map(EntityModel::of).toList();
        CollectionModel<EntityModel<TagDtoResponse>> modelCollection = CollectionModel.of(tagModels);
        modelCollection.forEach(LinkHelper::addLinksToTag);
        return modelCollection;
    }

    @Operation(summary = "Get comments by news id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received comments by news id"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/v1" + NEWS_BASE_URI + "/{id:\\d+}" + COMMENTS_BASE_URI)
    @Override
    public CollectionModel<EntityModel<CommentDtoResponse>> getCommentsByNewsId(@PathVariable Long id) {

        List<EntityModel<CommentDtoResponse>> commentModels = commentService.getByNewsId(id).stream().map(EntityModel::of).toList();
        CollectionModel<EntityModel<CommentDtoResponse>> modelCollection = CollectionModel.of(commentModels);
        modelCollection.forEach(LinkHelper::addLinksToComment);
        return modelCollection;
    }

    @Operation(summary = "Get author by news id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received the author by news id"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/v1" + NEWS_BASE_URI + "/{id:\\d+}" + AUTHORS_BASE_URI)
    @Override
    public EntityModel<AuthorDtoResponse> getAuthorByNewsId(@PathVariable Long id) {

        EntityModel<AuthorDtoResponse> model = EntityModel.of(authorService.getByNewsId(id));
        LinkHelper.addLinksToAuthor(model);
        return model;
    }
}
