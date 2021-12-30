package com.wwt.webapp.userwebapp.service.response;

import com.wwt.webapp.userwebapp.domain.relational.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class UserResponseList  extends BasicSuccessResponse {

    private List<UserResponseItem> users;

    @SuppressWarnings("WeakerAccess")
    public UserResponseList() {
        users = new ArrayList<>();
    }

    public List<UserResponseItem> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponseItem> users) {
        this.users = users;
    }

    private void addUser(UserResponseItem user) {
        users.add(user);
    }


    public static UserResponseList convertToUserResponseList(List<UserEntity> users) {
        UserResponseList l = new UserResponseList();
        for (UserEntity user : users) {
            l.addUser(new UserResponseItem(user.getUuid(), user.getCreatedAt(), user.getVersion(), user.getLoginId(), user.getEmailAddress(),
                    user.getEmailChangedAt(), user.getPasswordChangedAt(), user.getLastLoggedInAt(),user.getAdminRole(),user.getActivationStatus()));
        }
        return l;
    }
}
