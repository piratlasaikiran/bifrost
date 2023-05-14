package org.bhavani.constructions.services;

import org.bhavani.constructions.dao.entities.SiteEntity;
import org.bhavani.constructions.dto.CreateSiteRequestDTO;

public interface SiteService {

    SiteEntity createSite(CreateSiteRequestDTO createSiteRequestDTO);

    SiteEntity getSite(String siteName);

    SiteEntity updateSite(String siteName, CreateSiteRequestDTO createSiteRequestDTO);
}
