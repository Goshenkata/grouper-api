package com.example.grouperapi.service;

import com.example.grouperapi.model.entities.GroupEntity;

public interface GroupService {
    void seedGroups();

    GroupEntity getGroupByName(String name);
}
