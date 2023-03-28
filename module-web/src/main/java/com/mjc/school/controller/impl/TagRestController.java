package com.mjc.school.controller.impl;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.BaseController;
import com.mjc.school.hateoas.LinkHelper;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.query.TagServiceSearchParams;
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
import static com.mjc.school.constant.ApiConstant.TAGS_BASE_URI;

@RestController
@RequestMapping(value = API_BASE_URI, produces = MediaTypes.HAL_JSON_VALUE)
public class TagRestController implements BaseController<TagDtoRequest, TagDtoResponse, Long, JsonPatch, TagServiceSearchParams> {

    private final BaseService<TagDtoRequest, TagDtoResponse, Long, JsonPatch, TagServiceSearchParams> tagService;

    private final PagedResourcesAssembler<TagDtoResponse> pageAssembler;

    @Autowired
    public TagRestController(BaseService<TagDtoRequest, TagDtoResponse, Long, JsonPatch, TagServiceSearchParams> tagService,
                             PagedResourcesAssembler<TagDtoResponse> pagedAssembler) {
        this.tagService = tagService;
        this.pageAssembler = pagedAssembler;
    }

    @Operation(summary = "Get all tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received all tags"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v1" + TAGS_BASE_URI)
    @Override
    public PagedModel<EntityModel<TagDtoResponse>> getAll(
            @PageableDefault(size = 2)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable,
            TagServiceSearchParams params) {

        PagedModel<EntityModel<TagDtoResponse>> modelPage = pageAssembler.toModel(tagService.getAll(pageable, params));
        modelPage.forEach(LinkHelper::addLinksToTag);
        return modelPage;
    }

    @Operation(summary = "Get tag by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received a tag by its id"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/v1" + TAGS_BASE_URI + "/{id:\\d+}")
    @Override
    public EntityModel<TagDtoResponse> getById(@PathVariable Long id) {

        EntityModel<TagDtoResponse> model = EntityModel.of(tagService.getById(id));
        LinkHelper.addLinksToTag(model);
        return model;
    }

    @Operation(summary = "Create a tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag was created successfully"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/v1" + TAGS_BASE_URI, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public EntityModel<TagDtoResponse> create(@RequestBody TagDtoRequest createRequest) {

        EntityModel<TagDtoResponse> model = EntityModel.of(tagService.create(createRequest));
        LinkHelper.addLinksToTag(model);
        return model;
    }

    @Operation(summary = "Update a tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag was updated successfully"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/v1" + TAGS_BASE_URI + "/{id:\\d+}", consumes = "application/json-patch+json")
    @Override
    public EntityModel<TagDtoResponse> update(@PathVariable Long id, @RequestBody JsonPatch patch) {

        EntityModel<TagDtoResponse> model = EntityModel.of(tagService.update(id, patch));
        LinkHelper.addLinksToTag(model);
        return model;
    }

    @Operation(summary = "Delete tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag was deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/v1" + TAGS_BASE_URI + "/{id:\\d+}")
    @Override
    public void deleteById(@PathVariable Long id) {
        tagService.deleteById(id);
    }
}
