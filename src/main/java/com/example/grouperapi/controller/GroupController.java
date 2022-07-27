package com.example.grouperapi.controller;

import com.example.grouperapi.model.dto.GroupDTO;
import com.example.grouperapi.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/group/")
public class GroupController {
    private final GroupService groupService;

    @GetMapping("{name}")
    public ResponseEntity<GroupDTO> group(@PathVariable String name) {
        if (groupService != null && !groupService.exitstByName(name)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groupService.getGroupInfo(name));
    }
}
