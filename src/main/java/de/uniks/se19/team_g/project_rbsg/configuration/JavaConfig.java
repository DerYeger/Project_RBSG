package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.model.UserManager;
import de.uniks.se19.team_g.project_rbsg.termination.Terminator;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;

/**
 * @author Juri Lozowoj
 */

@Configuration
public class JavaConfig implements ApplicationContextAware {

    public static final int ICON_SIZE = 40;

    private ApplicationContext context;

    @Bean
    public Terminator terminator() {
        return new Terminator()
                .register(context.getBean(SceneManager.class))
                .register(context.getBean(UserManager.class));
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
