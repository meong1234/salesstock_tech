package com.ap.config.persistence.nosql.mongo;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

public class DateCreatorMongoEventListener extends AbstractMongoEventListener<AbstractDocument> {

	@Override
    public void onBeforeConvert(BeforeConvertEvent<AbstractDocument> event) {
        super.onBeforeConvert(event);

        AbstractDocument document = event.getSource();

        LocalDateTime d = LocalDateTime.now();
        // set created date if necessary
        if (document.getCreatedDate() == null) {
            document.setCreatedDate(d);
        }

        // update last updated date
        document.setLastUpdate(d);
    }
}
