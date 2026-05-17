package com.campus.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class TagDTO {
    private Long imageId;
    private List<String> tags;
}