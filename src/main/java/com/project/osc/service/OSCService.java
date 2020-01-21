package com.project.osc.service;

import com.project.osc.mapper.CampaignMapper;
import com.project.osc.mapper.OSCMapper;
import com.project.osc.model.Campaign;
import com.project.osc.service.http.CampaignResponse;
import com.project.osc.service.http.OSCResponse;
import com.project.osc.utils.ListCampaignValidator;
import com.project.osc.utils.ListOSCValidator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

import static com.project.osc.utils.Utils.validateIdNumber;

@RestController
public class OSCService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OSCService.class);
    private final OSCMapper oscMapper;
    private final CampaignMapper campaignMapper;
    private final ListOSCValidator listOSCValidator;
    private final ListCampaignValidator listCampaignValidator;
    private static final String INVALID_PARAMETER = "The parameters entered are invalid: ";
    private static final String EXCEPTION_MESSAGE = "An error occurred while trying to get historical indicators for employee id {}";

    @Autowired
    public OSCService(OSCMapper oscMapper, CampaignMapper campaignMapper, ListOSCValidator listOSCValidator, ListCampaignValidator listCampaignValidator) {
        this.oscMapper = oscMapper;
        this.campaignMapper = campaignMapper;
        this.listOSCValidator = listOSCValidator;
        this.listCampaignValidator = listCampaignValidator;
    }

    @GetMapping(
        value = "employee/indicators/osc/{idEmployee}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Obtener OSCs del empleado")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Se obtienen los datos de las OSCs por empleado", response = OSCResponse.class),
            @ApiResponse(code = 204, message = "El empleado ingresado es incorrecto", response = OSCResponse.class),
            @ApiResponse(code = 400, message = "Argumentos inválidos", response = OSCResponse.class),
            @ApiResponse(code = 500, message = "Error inesperado del servicio web", response = OSCResponse.class)
    })
    public ResponseEntity<OSCResponse> obtainOSCList(@PathVariable Integer idEmployee) {
        try{
            validateIdNumber(idEmployee);
            return Optional.ofNullable(oscMapper.obtainOSCList(idEmployee))
                    .map(listOSCValidator.obtainListValidator())
                    .orElseThrow( ()-> new RuntimeException("Something bad happened"));
        }catch (IllegalArgumentException iae){
            LOGGER.warn(INVALID_PARAMETER,iae);
            return ResponseEntity.badRequest().body(new OSCResponse(iae.getMessage()));
        }catch (Exception ex) {
            LOGGER.error(EXCEPTION_MESSAGE+idEmployee);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OSCResponse(ex.getMessage()));
        }
    }
    @GetMapping(
            value = "employee/indicators/campaign/{idEmployee}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Obtener campanias del empleado")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Se obtienen los datos de las campanias por empleado", response = CampaignResponse.class),
            @ApiResponse(code = 204, message = "El empleado ingresado es incorrecto", response = CampaignResponse.class),
            @ApiResponse(code = 400, message = "Argumentos inválidos", response = Campaign.class),
            @ApiResponse(code = 500, message = "Error inesperado del servicio web", response = CampaignResponse.class)
    })
    public ResponseEntity<CampaignResponse> obtainCampaignList (@PathVariable Integer idEmployee) {
        try{
            validateIdNumber(idEmployee);
            return Optional.of(campaignMapper.obtainCampaignList(idEmployee))
                    .map(listCampaignValidator.obtainListValidator())
                    .orElseThrow(()-> new RuntimeException("Something bad happened"));

        }catch (IllegalArgumentException iae){
            LOGGER.warn(INVALID_PARAMETER,iae);
            return ResponseEntity.badRequest().body(new CampaignResponse(iae.getMessage()));
        }catch (Exception ex) {
            LOGGER.error(EXCEPTION_MESSAGE+idEmployee);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CampaignResponse(ex.getMessage()));
        }
    }
}
