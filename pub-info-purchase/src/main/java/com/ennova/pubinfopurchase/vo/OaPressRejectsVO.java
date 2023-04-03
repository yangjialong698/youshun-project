package com.ennova.pubinfopurchase.vo;

import com.ennova.pubinfopurchase.dto.OaPressRejectsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yangjialong
 * @version 1.0
 * @date 2023/4/3
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OaPressRejectsVO {

    List<OaPressRejectsDTO> oaPressRejectsDTOList;

}
