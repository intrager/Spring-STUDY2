package me.brucehan.restfulhan.common;

import me.brucehan.restfulhan.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsResource extends EntityModel<Errors> {
    public ErrorsResource(Errors errors) {
    }

    public static EntityModel<Errors> modelOf(Errors errors) {
        EntityModel<Errors> errorsModel = EntityModel.of(errors);
        // IndexController에 있는 메서드에서 index라는 메서드로 가는 링크를 index라는 릴레이션으로 추가해주는 에러 리소스
        errorsModel.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
        return errorsModel;
    }
}
