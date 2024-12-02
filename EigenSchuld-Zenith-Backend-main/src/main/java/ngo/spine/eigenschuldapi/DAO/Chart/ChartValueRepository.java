package ngo.spine.eigenschuldapi.DAO.Chart;

import ngo.spine.eigenschuldapi.Model.Chart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChartValueRepository extends JpaRepository<Chart.Value, UUID> {
}
