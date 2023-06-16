package org.bhavani.constructions.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.bhavani.constructions.dao.api.VehicleEntityDao;
import org.bhavani.constructions.dao.entities.VehicleEntity;
import org.bhavani.constructions.helpers.AbstractDAO;
import org.bhavani.constructions.helpers.PageRequestUtil;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.bhavani.constructions.constants.Constants.VEHICLE_NUMBER;

@Slf4j
public class VehicleEntityDaoImpl extends AbstractDAO<VehicleEntity> implements VehicleEntityDao {

    @Inject
    public VehicleEntityDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void saveVehicleEntity(VehicleEntity vehicleEntity) {
        persist(vehicleEntity);
    }

    @Override
    public Optional<VehicleEntity> getVehicle(String vehicleNumber) {
        Map<String, Object> params = new HashMap<>();
        log.info("Fetching vehicle: {}", vehicleNumber);
        params.put(VEHICLE_NUMBER, vehicleNumber);
        return findOneByNamedQuery("GetVehicleByNumber", params);
    }

    @Override
    public void deleteVehicle(VehicleEntity vehicle) {
        this.currentSession().delete(vehicle);
    }

    @Override
    public List<VehicleEntity> getVehicles() {
        Map<String, Object> params = new HashMap<>();
        return findAllByNamedQuery("GetAllVehicles",
                params, PageRequestUtil.getDefaultPageRequest()).getContent();
    }
}
