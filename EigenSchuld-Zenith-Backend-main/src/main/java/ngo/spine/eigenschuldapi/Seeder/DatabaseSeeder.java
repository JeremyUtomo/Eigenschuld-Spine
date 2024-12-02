package ngo.spine.eigenschuldapi.Seeder;

import ngo.spine.eigenschuldapi.Model.Domain;
import ngo.spine.eigenschuldapi.Model.InvitationToken;
import ngo.spine.eigenschuldapi.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder {

    @Autowired
    private UserExerciseSeeder userExerciseSeeder;
    @Autowired
    private UserSeeder userSeeder;
    @Autowired
    private DomainSeeder domainSeeder;
    @Autowired
    private HulpverlenerSeeder hulpverlenerSeeder;

    private boolean alreadySeeded = false;


    @EventListener
    public void seed(ContextRefreshedEvent event){
        if(alreadySeeded){
            return;
        }

//        chartSeeder.seed();
//        userExerciseSeeder.seed();
        userExerciseSeeder.seed();
        Domain domain = domainSeeder.seed();
        User hulpverlener = hulpverlenerSeeder.seed(domain);
        userSeeder.seed(hulpverlener);


        this.alreadySeeded = true;
    }
}
