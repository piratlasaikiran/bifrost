package org.bhavani.constructions.inject;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.hibernate.HibernateBundle;
import lombok.RequiredArgsConstructor;
import org.bhavani.constructions.config.ServerConfiguration;
import org.bhavani.constructions.dao.api.*;
import org.bhavani.constructions.dao.impl.*;
import org.bhavani.constructions.serviceImpls.*;
import org.bhavani.constructions.services.*;
import org.hibernate.SessionFactory;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

@RequiredArgsConstructor
public class ServerModule extends DropwizardAwareModule<ServerConfiguration> {

    private final HibernateBundle<ServerConfiguration> hibernateBundle;

    @Override
    protected void configure() {
        bind(SupervisorService.class).to(DefaultSupervisorService.class).in(Singleton.class);
        bind(DriverService.class).to(DefaultDriverService.class).in(Singleton.class);
        bind(VehicleService.class).to(DefaultVehicleService.class).in(Singleton.class);
        bind(SiteService.class).to(DefaultSiteService.class).in(Singleton.class);
        bind(AssetManagementService.class).to(DefaultAssetManagementService.class).in(Singleton.class);
        bind(UserService.class).to(DefaultUserService.class).in(Singleton.class);
        bind(VendorService.class).to(DefaultVendorService.class).in(Singleton.class);
        bind(BankAccountService.class).to(DefaultBankAccountService.class).in(Singleton.class);
        bind(TransactionService.class).to(DefaultTransactionService.class).in(Singleton.class);
        bind(VendorAttendanceService.class).to(DefaultVendorAttendanceService.class).in(Singleton.class);
        bind(EmployeeAttendanceService.class).to(DefaultEmployeeAttendanceService.class).in(Singleton.class);

        bind(SupervisorEntityDao.class).to(SupervisorEntityDaoImpl.class).in(Singleton.class);
        bind(DriverEntityDao.class).to(DriverEntityDaoImpl.class).in(Singleton.class);
        bind(VehicleEntityDao.class).to(VehicleEntityDaoImpl.class).in(Singleton.class);
        bind(VehicleTaxEntityDao.class).to(VehicleTaxEntityDaoImpl.class).in(Singleton.class);
        bind(SiteEntityDao.class).to(SiteEntityDaoImpl.class).in(Singleton.class);
        bind(AssetLocationEntityDao.class).to(AssetLocationEntityDaoImpl.class).in(Singleton.class);
        bind(UserEntityDao.class).to(UserEntityDaoImpl.class).in(Singleton.class);
        bind(VendorEntityDao.class).to(VendorEntityDaoImpl.class).in(Singleton.class);
        bind(BankAccountEntityDao.class).to(BankAccountEntityDaoImpl.class).in(Singleton.class);
        bind(TransactionEntityDao.class).to(TransactionEntityDaoImpl.class).in(Singleton.class);
        bind(VendorAttendanceEntityDao.class).to(VendorAttendanceEntityDaoImpl.class).in(Singleton.class);
        bind(EmployeeAttendanceDao.class).to(EmployeeAttendanceDaoImpl.class).in(Singleton.class);
        bind(PassBookEntityDao.class).to(PassBookEntityDaoImpl.class).in(Singleton.class);
        bind(AssetOwnershipEntityDao.class).to(AssetOwnershipEntityDaoImpl.class).in(Singleton.class);
        bind(PendingBalanceEntityDao.class).to(PendingBalanceEntityDaoImpl.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    protected SessionFactory providesSessionFactory() {
        return hibernateBundle.getSessionFactory();
    }

}
