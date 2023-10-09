package com.qcadoo.mes.productFlowThruDivision.hooks;

import com.google.common.collect.Lists;
import com.qcadoo.mes.basic.constants.ProductFields;
import com.qcadoo.mes.basicProductionCounting.constants.ProductionCountingQuantityFields;
import com.qcadoo.mes.materialFlowResources.constants.ResourceFields;
import com.qcadoo.mes.productFlowThruDivision.constants.OrderProductResourceReservationFields;
import com.qcadoo.mes.productFlowThruDivision.reservation.OrderReservationsService;
import com.qcadoo.model.api.DataDefinition;
import com.qcadoo.model.api.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderProductResourceReservationHooks {

    public static final String L_RESOURCE = "resource";

    @Autowired
    private OrderReservationsService orderReservationsService;

    public void onSave(final DataDefinition orderProductResourceReservationDD, final Entity orderProductResourceReservation) {
        if(Objects.isNull(orderProductResourceReservation.getId())) {
            orderProductResourceReservation.setField(OrderProductResourceReservationFields.CREATION_DATE, new Date());
        }
        Entity resource = orderProductResourceReservation.getBelongsToField(L_RESOURCE);
        if (Objects.nonNull(resource)) {
            orderProductResourceReservation.setField("resourceNumber", resource.getStringField(ResourceFields.NUMBER));
            orderProductResourceReservation.setField("resourceUnit", resource.getBelongsToField(ResourceFields.PRODUCT).getStringField(ProductFields.UNIT));
        }
        orderReservationsService.createOrUpdateReservation(orderProductResourceReservation);
    }

    public boolean validate(final DataDefinition dataDefinition, final Entity orderProductResourceReservation) {

        Entity resource = orderProductResourceReservation.getBelongsToField(L_RESOURCE);

        if (Objects.isNull(resource)) {
            orderProductResourceReservation.addError(dataDefinition.getField(L_RESOURCE), "qcadooView.validate.field.error.missing");
            return false;
        }

        BigDecimal resourceQuantity = resource.getDecimalField(ResourceFields.AVAILABLE_QUANTITY);
        BigDecimal planedQuantity = orderProductResourceReservation.getDecimalField("planedQuantity");
        if (planedQuantity.compareTo(resourceQuantity) > 0) {
            orderProductResourceReservation.addError(dataDefinition.getField("planedQuantity"), "productFlowThruDivision.orderProductResourceReservation.error.planedQuantityGreaterThanResource");
            return false;
        }

        Entity pcq = orderProductResourceReservation.getBelongsToField("productionCountingQuantity");

        List<Entity> orderProductResourceReservations = Lists.newArrayList(pcq.getHasManyField("orderProductResourceReservations"));

        if (Objects.nonNull(orderProductResourceReservation.getId())) {
            orderProductResourceReservations = orderProductResourceReservations
                    .stream()
                    .filter(oprr -> !oprr.getId().equals(orderProductResourceReservation.getId()))
                    .collect(Collectors.toList());
        }
        orderProductResourceReservations.add(orderProductResourceReservation);

        BigDecimal productPlannedQuantity = pcq.getDecimalField(ProductionCountingQuantityFields.PLANNED_QUANTITY);

        BigDecimal plannedQuantityFromResources = orderProductResourceReservations
                .stream()
                .map(rr -> rr.getDecimalField("planedQuantity"))
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (plannedQuantityFromResources.compareTo(productPlannedQuantity) > 0) {
            orderProductResourceReservation.addError(dataDefinition.getField("planedQuantity"), "productFlowThruDivision.orderProductResourceReservation.error.allPlanedQuantityGreaterThanProductQuantity");
            return false;
        }


        return true;
    }
}