package org.bhavani.constructions.dao.impl;

import org.bhavani.constructions.dao.api.VehicleTaxEntityDao;
import org.bhavani.constructions.dao.entities.VehicleTaxEntity;
import org.bhavani.constructions.dao.entities.models.VehicleTaxEnum;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.*;

public class VehicleTaxEntityDaoImpl extends AbstractDAO<VehicleTaxEntity> implements VehicleTaxEntityDao {

    @Inject
    public VehicleTaxEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void saveVehicleTaxEntities(List<VehicleTaxEntity> vehicleTaxEntityList) {
        vehicleTaxEntityList.forEach(this::saveVehicleTaxEntity);
    }

    @Override
    public Optional<VehicleTaxEntity> getVehicleTaxEntity(String vehicleNumber, VehicleTaxEnum taxType, LocalDate validityStartDate) {
        Map<String, Object> params = new HashMap<>();
        params.put(VEHICLE_NUMBER, vehicleNumber);
        params.put(TAX_TYPE, taxType);
        params.put(START_DATE, validityStartDate);
        return findOneByNamedQuery("GetTaxEntryByDetails", params);
    }

    @Override
    public void deleteVehicleTaxEntry(VehicleTaxEntity vehicleTaxEntity) {
        this.currentSession().delete(vehicleTaxEntity);
    }

    public void saveVehicleTaxEntity(VehicleTaxEntity vehicleTaxEntity) {
        this.currentSession().saveOrUpdate(vehicleTaxEntity);
    }

    @Override
    public List<VehicleTaxEntity> getLatestTaxTypesForAllVehicles() {
        Map<String, Object> params = new HashMap<>();
        return findAllByNamedQuery("LatestTaxTypesByVehicleNumber",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }
}
