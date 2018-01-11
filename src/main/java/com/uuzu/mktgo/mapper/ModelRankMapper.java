
package com.uuzu.mktgo.mapper;

        import com.uuzu.mktgo.pojo.Model;
        import org.apache.ibatis.annotations.Param;
        import org.springframework.stereotype.Repository;
        import tk.mybatis.mapper.common.Mapper;

        import java.util.List;

@Repository
public interface ModelRankMapper extends Mapper<Model> {

    List<String> queryModelsByBrand(@Param("brand") String brand);
}
