package com.continuum.cucumber.ui.pages;

import com.continuum.cucumber.ui.pages.blocks.external.O365LoginBlock;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class O365LoginPage extends BasePage<O365LoginPage> {

    @Autowired
    private O365LoginBlock o365LoginBlock;
}
