package ngo.spine.eigenschuldapi.Controller;

import ngo.spine.eigenschuldapi.DTO.ResponseDTO;
import ngo.spine.eigenschuldapi.Filter.HasRightsToUse;
import ngo.spine.eigenschuldapi.Model.Response;
import ngo.spine.eigenschuldapi.Services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/question")
public class ResponseController {

    private final ResponseService responseService;

    @Autowired
    public ResponseController(ResponseService responseService) {
        this.responseService = responseService;
    }

    @HasRightsToUse
    @PostMapping("/response/save/{questionaryId}")
    public ResponseEntity<ResponseDTO> saveResponse(@RequestBody Response response) {
        Response savedResponse = this.responseService.saveResponse(response);
        ResponseDTO responseDTO = new ResponseDTO(savedResponse.getId(), savedResponse.getResponse(), savedResponse.getUser(), savedResponse.getResponseTime());
        return ResponseEntity.ok(responseDTO);
    }

    @HasRightsToUse
    @PutMapping("/response/update/{responseId}/{questionaryId}")
    public ResponseEntity<ResponseDTO> updateResponse(@PathVariable UUID responseId, @RequestBody Response updatedResponse) {
        Response response = responseService.updateResponse(responseId, updatedResponse);
        ResponseDTO responseDTO = new ResponseDTO(response.getId(), response.getResponse(), response.getUser(), response.getResponseTime());
        return ResponseEntity.ok(responseDTO);
    }
}
