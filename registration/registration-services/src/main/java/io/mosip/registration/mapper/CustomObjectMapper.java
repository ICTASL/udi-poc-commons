package io.mosip.registration.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import io.mosip.registration.dto.MachineMasterDTO;
import io.mosip.registration.dto.RegCenterUserDTO;
import io.mosip.registration.dto.UserBiometricDTO;
import io.mosip.registration.dto.UserDTO;
import io.mosip.registration.dto.UserMachineMappingDTO;
import io.mosip.registration.dto.UserRoleDTO;
import io.mosip.registration.dto.demographic.AddressDTO;
import io.mosip.registration.dto.demographic.DemographicDTO;
import io.mosip.registration.dto.demographic.DemographicInfoDTO;
import io.mosip.registration.dto.json.demo.Address;
import io.mosip.registration.dto.json.demo.Demographic;
import io.mosip.registration.dto.json.demo.DemographicInfo;
import io.mosip.registration.entity.MachineMaster;
import io.mosip.registration.entity.RegCenterUser;
import io.mosip.registration.entity.UserBiometric;
import io.mosip.registration.entity.UserDetail;
import io.mosip.registration.entity.UserMachineMapping;
import io.mosip.registration.entity.UserRole;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.ConfigurableMapper;

/**
 * Class for Orika Object Mapper
 * 
 * @author YASWANTH S
 * @author Balaji Sridharan
 * @since 1.0.0
 */
public class CustomObjectMapper extends ConfigurableMapper {

	/**
	 * Instance of MapperFacade for object mapping
	 */
	public static final MapperFacade MAPPER_FACADE = new CustomObjectMapper();

	/**
	 * Method to configure the Orika Mapper for object conversions
	 * 
	 * @param mapperFactory the Orika MapperFactory
	 */
	@Override
	public void configure(MapperFactory mapperFactory) {

		ConverterFactory converterFactory = mapperFactory.getConverterFactory();
		converterFactory.registerConverter(new PassThroughConverter(LocalDateTime.class));
		converterFactory.registerConverter(new PassThroughConverter(OffsetDateTime.class));
		converterFactory.registerConverter("packetMetaInfo", new PacketMetaInfoConverter());

		mapperFactory.classMap(DemographicInfoDTO.class, DemographicInfo.class).byDefault().register();

		mapperFactory.classMap(AddressDTO.class, Address.class).byDefault().register();

		mapperFactory.classMap(DemographicDTO.class, Demographic.class).exclude("applicantDocumentDTO")
				.exclude("introducerRID").exclude("introducerUIN").byDefault().register();
		
		mapperFactory.classMap(UserRole.class, UserRoleDTO.class)
		.customize(new CustomMapper<UserRole, UserRoleDTO>() {
			@Override
			public void mapAtoB(UserRole a, UserRoleDTO b, MappingContext context) {
				b.setUsrId(a.getUserRoleID().getUsrId());
				b.setRoleCode(a.getUserRoleID().getRoleCode());
				b.setLangCode(a.getLangCode());
				b.setActive(a.getIsActive());
			}
		}).byDefault().register();
		
		mapperFactory.classMap(MachineMaster.class, MachineMasterDTO.class)
		.customize(new CustomMapper<MachineMaster, MachineMasterDTO>() {
			@Override
			public void mapAtoB(MachineMaster a, MachineMasterDTO b, MappingContext context) {
				b.setMacAddress(a.getMacAddress());
				b.setName(a.getName());
				b.setSerialNum(a.getSerialNum());
			}
		}).byDefault().register();
		
		mapperFactory.classMap(UserMachineMapping.class, UserMachineMappingDTO.class)
		.customize(new CustomMapper<UserMachineMapping, UserMachineMappingDTO>() {
			@Override
			public void mapAtoB(UserMachineMapping a, UserMachineMappingDTO b, MappingContext context) {
				b.setCentreID(a.getUserMachineMappingId().getCentreID());
				b.setMachineID(a.getUserMachineMappingId().getMachineID());
				b.setUserID(a.getUserMachineMappingId().getUserID());
				b.setLangCode(a.getLangCode());
				b.setActive(a.getIsActive());
			}
		}).byDefault().register();
		
		mapperFactory.classMap(UserBiometric.class, UserBiometricDTO.class)
		.customize(new CustomMapper<UserBiometric, UserBiometricDTO>() {
			@Override
			public void mapAtoB(UserBiometric a, UserBiometricDTO b, MappingContext context) {
				b.setUsrId(a.getUserBiometricId().getUsrId());
				b.setBioAttributeCode(a.getUserBiometricId().getBioAttributeCode());
				b.setBioTypeCode(a.getUserBiometricId().getBioTypeCode());
			}
		}).byDefault().register();
		
		mapperFactory.classMap(RegCenterUser.class, RegCenterUserDTO.class)
		.customize(new CustomMapper<RegCenterUser, RegCenterUserDTO>() {
			@Override
			public void mapAtoB(RegCenterUser a, RegCenterUserDTO b, MappingContext context) {
				b.setRegcntrId(a.getRegCenterUserId().getRegcntrId());
				b.setUsrId(a.getRegCenterUserId().getUsrId());
			}
		}).byDefault().register();
		
		
		mapperFactory.classMap(UserDetail.class, UserDTO.class).byDefault().register();
		mapperFactory.classMap(UserDTO.class, UserDetail.class).byDefault().register();
	}

}
