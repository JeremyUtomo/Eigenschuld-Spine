package ngo.spine.eigenschuldapi.DAO.Response;

import ngo.spine.eigenschuldapi.Model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ResponseDAO {

    private final ResponseRepository responseRepository;

    @Autowired
    public ResponseDAO(ResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }

    public Response save(Response response) {
        return this.responseRepository.save(response);
    }

    public void delete(UUID id) {
        this.responseRepository.deleteById(id);
    }
}
