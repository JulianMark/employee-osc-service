package com.project.osc.service;

import com.project.osc.mapper.CampaignMapper;
import com.project.osc.mapper.OSCMapper;
import com.project.osc.model.Campaign;
import com.project.osc.model.OSC;
import com.project.osc.service.http.CampaignResponse;
import com.project.osc.service.http.OSCResponse;
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

@RestController
public class OSCService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OSCService.class);
    private final OSCMapper oscMapper;
    private final CampaignMapper campaignMapper;

    @Autowired
    public OSCService(OSCMapper oscMapper, CampaignMapper campaignMapper) {
        this.oscMapper = oscMapper;
        this.campaignMapper = campaignMapper;
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
            validateIdEmployee(idEmployee);
            List<OSC> oscList = oscMapper.obtainOSCList(idEmployee);
            if (oscList.isEmpty()) {
                LOGGER.info("No se obtuvieron OSC para el empleado "+idEmployee);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new OSCResponse("OSCs no encontradas"));
            }
            LOGGER.info("Se obtuvieron las oscs para el empleado "+idEmployee);
            return ResponseEntity.ok(new OSCResponse(oscList));

        }catch (IllegalArgumentException iae){
            LOGGER.warn("Los parametros ingresados son invalidos: ",iae);
            return ResponseEntity.badRequest().body(new OSCResponse(iae.getMessage()));
        }catch (Exception ex) {
            LOGGER.error("Ocurrio un error al intentar obtener las OSC para el empleado con id: "+idEmployee);
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
            validateIdEmployee(idEmployee);
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

    private void validateIdEmployee (Integer idEmployee){
        if (idEmployee == null){
            throw new IllegalArgumentException("El id del empleado no puede ser nulo");
        }
        if (idEmployee <= 0){
            throw new IllegalArgumentException("El id del empleado no puede ser menor o igual a 0");
        }
    }
}
