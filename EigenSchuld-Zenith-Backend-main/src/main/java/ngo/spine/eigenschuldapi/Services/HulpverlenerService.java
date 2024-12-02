package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.Domain.*;
import ngo.spine.eigenschuldapi.DAO.User.*;
import ngo.spine.eigenschuldapi.DTO.*;
import ngo.spine.eigenschuldapi.Model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class HulpverlenerService {

    private final UserDAO userDAO;

    @Autowired
    public HulpverlenerService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<HulpverlenerDTO> getAllHulpverleners() {
        List<User> users = userDAO.getAllHulpverleners();
        List<HulpverlenerDTO> hulpverlenerDTO = new ArrayList<>();
        for (User user : users) {
            hulpverlenerDTO.add(user.getHulpverlenerDTO());
        }

        return hulpverlenerDTO;

    }

    public List<HulpverlenerDTO> getHulpverlenersByDomainId(String domainId) {
        List<User> users = userDAO.getAllHulpverleners();
        List<HulpverlenerDTO> hulpverlenerDTO = new ArrayList<>();

        for (User user : users) {
            if(user.getDomainId().toString().equals(domainId)) {
                hulpverlenerDTO.add(user.getHulpverlenerDTO());
            }
        }

        return hulpverlenerDTO;
    }
}
