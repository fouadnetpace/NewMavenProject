package models.usermanagement;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Role {

    @JsonProperty
    private String title;

    @JsonProperty
    private String description;

    @JsonProperty
    private boolean status;

    @JsonProperty
    private String createdBy;

    @JsonProperty
    private List<String> permissionIds;

    public Role(String title, String description, boolean status, String createdBy, List<String> permissionIds) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdBy = createdBy;
        this.permissionIds = permissionIds;
    }
}
