package com.project.osc.service;

import com.project.osc.mapper.CampaignMapper;
import com.project.osc.mapper.OSCMapper;
import com.project.osc.model.Campaign;
import com.project.osc.service.http.CampaignResponse;
import com.project.osc.service.http.OSCResponse;
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
import java.util.List;
import java.util.Optional;

import static com.project.osc.utils.Utils.validateIdNumber;

@RestController
public class OSCService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OSCService.class);
    private final OSCMapper oscMapper;
    private final CampaignMapper campaignMapper;
    private final ListOSCValidator listOSCValidator;

    @Autowired
    public OSCService(OSCMapper oscMapper, CampaignMapper campaignMapper, ListOSCValidator listOSCValidator) {
        this.oscMapper = oscMapper;
        this.campaignMapper = campaignMapper;
        this.listOSCValidator = listOSCValidator;
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
            LOGGER.warn("The parameters entered are invalid: ",iae);
            return ResponseEntity.badRequest().body(new OSCResponse(iae.getMessage()));
        }catch (Exception ex) {
            LOGGER.error("An error occurred while trying to obtain the osc for the employee with id: "+idEmployee);
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
            List<Campaign> campaignList = campaignMapper.obtainCampaignList(idEmployee);
            if (campaignList.isEmpty()) {
                LOGGER.info("No se obtuvieron campanias para el empleado "+idEmployee);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new CampaignResponse("Campanias no encontradas"));
            }
            LOGGER.info("Se obtuvieron las campanias para el empleado "+idEmployee);
            return ResponseEntity.ok(new CampaignResponse(campaignList));

        }catch (IllegalArgumentException iae){
            LOGGER.warn("Los parametros ingresados son invalidos: ",iae);
            return ResponseEntity.badRequest().body(new CampaignResponse(iae.getMessage()));
        }catch (Exception ex) {
            LOGGER.error("Ocurrio un error al intentar obtener las campanias para el empleado con id: "+idEmployee);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CampaignResponse(ex.getMessage()));
        }
    }
}
