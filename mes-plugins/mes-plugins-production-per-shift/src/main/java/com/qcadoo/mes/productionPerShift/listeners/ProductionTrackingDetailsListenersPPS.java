/**
 * ***************************************************************************
 * Copyright (c) 2010 Qcadoo Limited
 * Project: Qcadoo MES
 * Version: 1.4
 *
 * This file is part of Qcadoo.
 *
 * Qcadoo is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * ***************************************************************************
 */
package com.qcadoo.mes.productionPerShift.listeners;

import com.google.common.collect.Maps;
import com.qcadoo.mes.productionCounting.constants.ProductionTrackingFields;
import com.qcadoo.model.api.Entity;
import com.qcadoo.view.api.ComponentState;
import com.qcadoo.view.api.ViewDefinitionState;
import com.qcadoo.view.api.components.FormComponent;
import com.qcadoo.view.constants.QcadooViewConstants;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class ProductionTrackingDetailsListenersPPS {

    public final void showChangeover(final ViewDefinitionState view, final ComponentState state, final String[] args) {
        FormComponent productionTrackingForm = (FormComponent) view.getComponentByReference(QcadooViewConstants.L_FORM);

        Long productionTrackingId = productionTrackingForm.getEntityId();

        if (Objects.nonNull(productionTrackingId)) {
            Entity productionTracking = productionTrackingForm.getEntity();

            Entity order = productionTracking.getBelongsToField(ProductionTrackingFields.ORDER);

            Long orderId = order.getId();

            if (Objects.isNull(orderId)) {
                return;
            }

            Map<String, Object> parameters = Maps.newHashMap();
            parameters.put("form.id", orderId);
            parameters.put("window.activeMenu", "ordersTracking.productionTrackingsList");

            String url = "/page/lineChangeoverNormsForOrders/lineChangeoverNormsForOrderDetails.html";
            view.redirectTo(url, false, true, parameters);
        }
    }

}
