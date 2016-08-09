package com.ap.config.persistence.jpa;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.ap.misc.springutil.SecurityUtils;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        return SecurityUtils.getCurrentUserLogin();
    }
}
