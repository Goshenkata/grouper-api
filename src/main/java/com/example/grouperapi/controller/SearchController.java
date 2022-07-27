package com.example.grouperapi.controller;

import com.example.grouperapi.model.dto.ObjectSearchReturnDTO;
import com.example.grouperapi.model.dto.ObjectTypeEnum;
import com.example.grouperapi.service.GroupService;
import com.example.grouperapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/api/")
public class SearchController {
    private final GroupService groupService;
    private final UserService userService;

    @GetMapping("get-object")
    public ResponseEntity searchResult(@RequestParam String query, @RequestParam ObjectTypeEnum objectType) {
        if (query.isBlank()) return ResponseEntity.badRequest().body("query can't be empty");
        if (objectType == null) {
            return ResponseEntity.badRequest()
                    .body("Object type can only be " + Arrays.stream(ObjectTypeEnum.values())
                            .map(ObjectTypeEnum::name).collect(Collectors.joining(", ")));
        }
        List<ObjectSearchReturnDTO> searchResult = new ArrayList<>();
        switch (objectType) {
            case GROUP -> searchResult = groupService.getGroupSearch(query);
            case USER -> searchResult = userService.getUserSearch(query);
        }
        return ResponseEntity.ok(searchResult);
    }


}
