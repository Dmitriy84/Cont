package com.continuum.cucumber.ui.pages;

import com.continuum.cucumber.ui.pages.blocks.HeaderBlock;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class BaseContinuumPage extends BasePage<BaseContinuumPage> {
    @Autowired
    private HeaderBlock headerBlock;
}