package de.uniks.se19.team_g.project_rbsg.configuration;

import io.rincl.Rincl;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class LocaleConfig {

    @Bean
    public Property<Locale> selectedLocale()
    {
        final SimpleObjectProperty<Locale> selectedLocale = new SimpleObjectProperty<>();

        selectedLocale.addListener(observable -> {
            Rincl.setLocale(selectedLocale.get());
        });

        return selectedLocale;
    }
}
