package com.timeseries.seriestemporelles.repository;

import com.timeseries.seriestemporelles.model.EventModel;
import com.timeseries.seriestemporelles.model.TagsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagsRepository extends JpaRepository <TagsModel, Integer> {

    @Query(value = "select tags from TagsModel tags where tags.event = ?1")
    List<TagsModel> findTagsOfEvent(EventModel event);

    @Query(value = "select tag from TagsModel tag where tag.event = ?1 and tag.label = ?2")
    TagsModel getTagByEventLabel(EventModel event, String label);

    @Query(value = "select tags.event from TagsModel tags where tags.label = ?1")
    List<EventModel> findEventsByTag(String label);
}
