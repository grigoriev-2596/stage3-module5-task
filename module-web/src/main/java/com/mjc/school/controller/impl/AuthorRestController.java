package com.mjc.school.controller.impl;

import com.github.fge.jsonpatch.JsonPatch;
import com.mjc.school.controller.AuthorController;
import com.mjc.school.hateoas.LinkHelper;
import com.mjc.school.service.AuthorService;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.dto.AuthorWithNewsResponse;
import com.mjc.school.service.query.AuthorServiceSearchParams;
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
import static com.mjc.school.constant.ApiConstant.AUTHORS_BASE_URI;

@RestController
@RequestMapping(value = API_BASE_URI, produces = MediaTypes.HAL_JSON_VALUE)
public class AuthorRestController implements AuthorController {

    private final AuthorService authorService;

    private final PagedResourcesAssembler<AuthorDtoResponse> pageAssembler;
    private final PagedResourcesAssembler<AuthorWithNewsResponse> authorWithNewsPageAssembler;

    @Autowired
    public AuthorRestController(AuthorService authorService,
                                PagedResourcesAssembler<AuthorDtoResponse> pageAssembler,
                                PagedResourcesAssembler<AuthorWithNewsResponse> authorWithNewsPageAssembler) {
        this.authorService = authorService;
        this.pageAssembler = pageAssembler;
        this.authorWithNewsPageAssembler = authorWithNewsPageAssembler;
    }

    @ApiOperation(value = "Get all authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received all authors"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v1" + AUTHORS_BASE_URI)
    @Override
    public PagedModel<EntityModel<AuthorDtoResponse>> getAll(
            @PageableDefault(size = 5)
            @SortDefault(sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable,
            AuthorServiceSearchParams param) {

        PagedModel<EntityModel<AuthorDtoResponse>> modelPage = pageAssembler.toModel(authorService.getAll(pageable, param));
        modelPage.forEach(LinkHelper::addLinksToAuthor);
        return modelPage;
    }

    @ApiOperation(value = "Get author by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received the author by its id"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/v1" + AUTHORS_BASE_URI + "/{id:\\d+}")
    @Override
    public EntityModel<AuthorDtoResponse> getById(@PathVariable Long id) {

        EntityModel<AuthorDtoResponse> model = EntityModel.of(authorService.getById(id));
        LinkHelper.addLinksToAuthor(model);
        return model;
    }

    @ApiOperation(value = "Create an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The author was created successfully"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/v1" + AUTHORS_BASE_URI, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public EntityModel<AuthorDtoResponse> create(@RequestBody AuthorDtoRequest createRequest) {

        EntityModel<AuthorDtoResponse> model = EntityModel.of(authorService.create(createRequest));
        LinkHelper.addLinksToAuthor(model);
        return model;
    }

    @ApiOperation(value = "Update an author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The author was updated successfully"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(value = "/v1" + AUTHORS_BASE_URI + "/{id:\\d+}", consumes = "application/json-patch+json")
    @Override
    public EntityModel<AuthorDtoResponse> update(@PathVariable Long id, @RequestBody JsonPatch patch) {

        EntityModel<AuthorDtoResponse> model = EntityModel.of(authorService.update(id, patch));
        LinkHelper.addLinksToAuthor(model);
        return model;
    }

    @ApiOperation(value = "Delete author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The author was deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/v1" + AUTHORS_BASE_URI + "/{id:\\d+}")
    @Override
    public void deleteById(@PathVariable Long id) {
        authorService.deleteById(id);
    }

    @ApiOperation(value = "Get authors with the amount of news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully retrieved authors with the amount of news"),
            @ApiResponse(responseCode = "400", description = "Application cannot process the request due to a client error"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/v1" + AUTHORS_BASE_URI + "/with-news-amount")
    @Override
    public PagedModel<EntityModel<AuthorWithNewsResponse>> getAuthorsWithNewsAmount(
            @PageableDefault(size = 5)
            Pageable pageable) {

        PagedModel<EntityModel<AuthorWithNewsResponse>> modelPage = authorWithNewsPageAssembler.toModel(authorService.getWithNewsAmount(pageable));
        modelPage.forEach(LinkHelper::addLinksToAuthorWithNewsAmount);
        return modelPage;
    }
}
