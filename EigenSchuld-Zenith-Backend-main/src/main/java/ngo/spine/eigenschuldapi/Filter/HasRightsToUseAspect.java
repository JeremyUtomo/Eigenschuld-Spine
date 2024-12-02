package ngo.spine.eigenschuldapi.Filter;

import jakarta.servlet.http.HttpServletRequest;
import ngo.spine.eigenschuldapi.DAO.Chart.ChartDAO;
import ngo.spine.eigenschuldapi.DAO.ExerciseChart.ExerciseChartDAO;
import ngo.spine.eigenschuldapi.DAO.Letter.LetterDAO;
import ngo.spine.eigenschuldapi.DAO.Questionary.QuestionaryDAO;
import ngo.spine.eigenschuldapi.Exception.UserDoesNotHaveRightsException;
import ngo.spine.eigenschuldapi.Filter.ExerciseAccessFilters.*;
import ngo.spine.eigenschuldapi.Model.User;
import ngo.spine.eigenschuldapi.Services.ChartService;
import ngo.spine.eigenschuldapi.Services.JwtService;
import ngo.spine.eigenschuldapi.Services.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HasRightsToUseAspect {
    private final HttpServletRequest request;
    private final LetterDAO letterDAO;
    private final JwtService jwtService;
    private final UserService userService;
    private final ChartDAO chartDAO;
    private final ChartService chartService;
    private final QuestionaryDAO questionaryDAO;
    private ExerciseAccessFilter exerciseAccessFilter = null;

    @Autowired
    public HasRightsToUseAspect(
        HttpServletRequest request,
        LetterDAO letterDAO,
        JwtService jwtService,
        UserService userService,
        ChartDAO chartDAO,
        ChartService chartService,
        QuestionaryDAO questionaryDAO
    ) {
        this.request = request;
        this.letterDAO = letterDAO;
        this.jwtService = jwtService;
        this.userService = userService;
        this.chartDAO = chartDAO;
        this.chartService = chartService;
        this.questionaryDAO = questionaryDAO;
    }

    @Around("@annotation(HasRightsToUse)")
    public Object HasRightsToUse(ProceedingJoinPoint joinPoint) throws Throwable {
        String[] splitRequest = request.getRequestURI().split("/");
        String endpoint = splitRequest[3];

        this.setStrategy(endpoint);
        User requestUser = this.getUser();

        boolean hasRights = this.exerciseAccessFilter.hasRights(request, requestUser, splitRequest);

        if (hasRights) {
            return joinPoint.proceed();
        } else {
            throw new UserDoesNotHaveRightsException();
        }
    }

    private User getUser() {
        String authToken = this.request.getHeader("Authorization");
        String email = jwtService.extractUsername(authToken.substring(7));

        return this.userService.getUserByEmail(email);
    }

    private void setStrategy(String endpoint) {
        switch (endpoint) {
            case "letter" -> this.exerciseAccessFilter = new LetterAccessFilter(this.letterDAO);
            case "chart" -> this.exerciseAccessFilter = new ChartAccessFilter(this.chartDAO, this.chartService);
            case "questionary" -> this.exerciseAccessFilter = new QuestionaryAccessFilter(this.questionaryDAO);
            case "question" -> this.exerciseAccessFilter = new ResponseAccessFilter(this.questionaryDAO);
            case "user" -> this.exerciseAccessFilter = new UserAccessFilter(this.userService);
            case "clientprogress" -> this.exerciseAccessFilter = new UserAccessFilter(this.userService);
        }
    }
}
