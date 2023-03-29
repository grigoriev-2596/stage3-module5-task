package com.mjc.school.controller.impl;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.BaseController;
import com.mjc.school.hateoas.LinkHelper;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.CommentDtoRequest;
import com.mjc.school.service.dto.CommentDtoResponse;
import com.mjc.school.service.query.CommentServiceSearchParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.mjc.school.constant.ApiConstant.API_BASE_URI;
import static com.mjc.school.constant.ApiConstant.COMMENTS_BASE_URI;

@RestController
@RequestMapping(value = API_BASE_URI, produces = MediaTypes.HAL_JSON_VALUE)
public class CommentRestController implements BaseController<CommentDtoRequest, CommentDtoResponse, Long, JsonPatch, CommentServiceSearchParams> {

    private final BaseService<CommentDtoRequest, CommentDtoResponse, Long, JsonPatch, CommentServiceSearchParams> commentService;

    private final PagedResourcesAssembler<CommentDtoResponse> pageAssembler;

    @Autowired
    public CommentRestController(BaseService<CommentDtoRequest, CommentDtoResponse, Long, JsonPatch, CommentServiceSearchParams> commentService,
                                 PagedResourcesAssembler<CommentDtoResponse> pageAssembler) {
        this.commentService = commentService;
        this.pageAssembler = pageAssembler;
    }

    @ApiOperation(value = "Get all comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received all comments"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v1" + COMMENTS_BASE_URI)
    public PagedModel<EntityModel<CommentDtoResponse>> getAll(
            @PageableDefault(size = 20)
            @SortDefault(sort = "creationDate", direction = Sort.Direction.DESC)
            Pageable pageable,
            CommentServiceSearchParams params) {

        PagedModel<EntityModel<CommentDtoResponse>> modelPage = pageAssembler.toModel(commentService.getAll(pageable, params));
        modelPage.forEach(LinkHelper::addLinksToComment);
        return modelPage;
    }

    @ApiOperation(value = "Get comment by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received a comment by its id"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/v1" + COMMENTS_BASE_URI + "/{id:\\d+}")
    @Override
    public EntityModel<CommentDtoResponse> getById(@PathVariable Long id) {

        EntityModel<CommentDtoResponse> model = EntityModel.of(commentService.getById(id));
        LinkHelper.addLinksToComment(model);
        return model;
    }

    @ApiOperation(value = "Create a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment was created successfully"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/v1" + COMMENTS_BASE_URI, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public EntityModel<CommentDtoResponse> create(@RequestBody CommentDtoRequest createRequest) {

        EntityModel<CommentDtoResponse> model = EntityModel.of(commentService.create(createRequest));
        LinkHelper.addLinksToComment(model);
        return model;
    }

    @ApiOperation(value = "Update a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment was updated successfully"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/v1" + COMMENTS_BASE_URI + "/{id:\\d+}", consumes = "application/json-patch+json")
    @Override
    public EntityModel<CommentDtoResponse> update(@PathVariable Long id, @RequestBody JsonPatch patch) {

        EntityModel<CommentDtoResponse> model = EntityModel.of(commentService.update(id, patch));
        LinkHelper.addLinksToComment(model);
        return model;
    }

    @ApiOperation(value = "Delete comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment was deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/v1" + COMMENTS_BASE_URI + "/{id:\\d+}")
    @Override
    public void deleteById(@PathVariable Long id) {
        commentService.deleteById(id);
    }


}
