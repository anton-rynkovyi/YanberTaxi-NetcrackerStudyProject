package com.netcracker.project.study.vaadin.admin.components.logo;

import com.github.appreciated.material.MaterialTheme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import org.springframework.stereotype.Component;

@Component
public class BottomTeamLogoLink extends CustomComponent {

    public static final String LINK_TEXT = "<b>Netcracker student team ©</b>";
    public static final String LINK_SRC = "https://github.com/Rynkovoy/YanberTaxi-NetcrackerStudyProject";

    public BottomTeamLogoLink() {
        VerticalLayout layoutContent = genLayoutContent();
        Link logoLink = new Link(LINK_TEXT, new ExternalResource(LINK_SRC));
        logoLink.setIcon(VaadinIcons.COGS);
        logoLink.setStyleName(MaterialTheme.LINK_SMALL);
        logoLink.setCaptionAsHtml(true);

        layoutContent.addComponent(logoLink);
        layoutContent.setComponentAlignment(logoLink, Alignment.BOTTOM_CENTER);
        setCompositionRoot(layoutContent);
    }

    private VerticalLayout genLayoutContent() {
        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.setMargin(false);
        layoutContent.setSpacing(false);
        layoutContent.setSizeFull();
        return layoutContent;
    }
}
