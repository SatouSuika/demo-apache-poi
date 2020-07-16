package com.cyc.poi.controller;
import cn.hutool.json.JSONUtil;
import com.cyc.poi.Mapper.*;
import com.cyc.poi.model.TlstCourseItem;
import com.cyc.poi.model.TlstCourseOptions;
import com.cyc.poi.model.TlstCoursePapper;
import com.cyc.poi.model.TlstCourseSection;
import com.cyc.poi.util.SimpleExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class EboxController {


    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @Autowired
    private TlstCourseSectionMapper tlstCourseSectionMapper;

    @Autowired
    private TlstCourseItemMapper tlstCourseItemMapper;

    @Autowired
    private TlstCourseOptionsMapper tlstCourseOptionsMapper;

    @Autowired
    private TlstCourseTypeMapper tlstCourseTypeMapper;

    @Autowired
    private TlstCoursePapperMapper tlstCoursePapperMapper;

    /**
     * 听力试题导入 tlst_course_type自行添加分类
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/importExcel")
    @ResponseBody
    @Transactional
    public String tlstImportExcel(@RequestParam("file") MultipartFile file,@RequestParam(value = "audioPrefix",required = false) String audioPrefix
            ,@RequestParam("courseType") Integer courseType) throws IOException {
        System.out.println("使用SimpleExcelUtils");
        List<Map<String, String>> maps = SimpleExcelUtils.readExcel(file);
//        maps.forEach(System.out::println);
        if(maps.size()==0){
            return "数据为空";
        }
        String paperName = file.getOriginalFilename();
        paperName = paperName.substring(0,paperName.lastIndexOf("."));

        Map<String,String> audioPrefixMap = new HashMap<>();
        audioPrefixMap.put("CET4-2018年6月第1套","cet4/18.6_1/");
        audioPrefixMap.put("CET4-2018年6月第2套","cet4/18.6_2/");
        audioPrefixMap.put("CET4-2018年12月第1套","cet4/18.12_1/");
        audioPrefixMap.put("CET4-2018年12月第2套","cet4/18.12_2/");
        audioPrefixMap.put("CET4-2019年6月第1套","cet4/19.6_1/");
        audioPrefixMap.put("CET4-2019年6月第2套","cet4/19.6_2/");
        audioPrefixMap.put("CET4-2019年12月第1套","cet4/19.12_1/");
        audioPrefixMap.put("CET4-2019年12月第2套","cet4/19.12_2/");

        audioPrefixMap.put("大学英语六级2018年6月(1)","cet6/18-6(1)/");
        audioPrefixMap.put("大学英语六级2018年6月(2)","cet6/18-6(2)/");
        audioPrefixMap.put("大学英语六级2018年12月(1)","cet6/18-12(1)/");
        audioPrefixMap.put("大学英语六级2018年12月(2)","cet6/18-12(2)/");
        audioPrefixMap.put("大学英语六级2019年6月(1)","cet6/19-6(1)/");
        audioPrefixMap.put("大学英语六级2019年6月(2)","cet6/19-6(2)/");
        audioPrefixMap.put("大学英语六级2019年12月(1)","cet6/19-12(1)/");
        audioPrefixMap.put("大学英语六级2019年12月(2)","cet6/19-12(2)/");


        if(StringUtils.isEmpty(audioPrefix)){
            if(audioPrefixMap.get(paperName) != null){
                audioPrefix = audioPrefixMap.get(paperName).toString();
            }else{
                return "请输入音频前缀文件夹，例:cet4/18.6_2/，其中对应服务器/ebox/listen/courseListen/**";
            }
        }
        if(courseType == null){
            return "输入tlst_course_type的id";
        }
        //音频前缀
//        String audioPrefix= "cet4_18.6_2/";
        //题型，默认为1选择题
        Integer questionType= 1;
        //题型得分
        Map<Integer,Integer> scoreMap = new HashMap<>();
        scoreMap.put(1,1);
        scoreMap.put(2,5);
        scoreMap.put(3,10);

        //tlst_course_papper表的name

        TlstCoursePapper tlstCoursePapper = new TlstCoursePapper();
        tlstCoursePapper.setType(courseType);//对应tlst_course_type，自行分类
        tlstCoursePapper.setName(paperName);
        tlstCoursePapper.setSubscribeTimes(0);//貌似无用字段
        tlstCoursePapper.setStatus(1);
        tlstCoursePapper.setSectionCount(0);//后续计算
        tlstCoursePapper.setItemCount(0);//后续计算
        tlstCoursePapper.setQuestionCount(0);//后续计算
        tlstCoursePapper.setIsTeachingPapper(1);//不会用，有需调整
        tlstCoursePapper.setIsPublic(0);//不会用，有需调整
        tlstCoursePapperMapper.insertSelective(tlstCoursePapper);


        List<TlstCourseSection> tlstCourseSections = new ArrayList<>();
        List<TlstCourseItem> tlstCourseItems = new ArrayList<>();
        //生成tlst_course_section数据
        LinkedHashMap<String,List<Map<String, String>>> sectionMap = maps.stream().filter(a->a.get("section") != null && StringUtils.isNotEmpty(a.get("section"))).collect(Collectors.groupingBy(a->a.get("section"), LinkedHashMap::new, Collectors.toList()));
        Integer sectionIndex = 1;
        Integer itemIndex = 1;
        Integer optionsIndex = 1;
        for (Map.Entry<String, List<Map<String, String>>> map :sectionMap.entrySet()) {
            String key = map.getKey();
            List<Map<String, String>> child =  map.getValue();
            TlstCourseSection tlstCourseSection = new TlstCourseSection();
            tlstCourseSection.setName("Section "+key);
            tlstCourseSection.setIndex(sectionIndex);
            tlstCourseSection.setCreateDatetime(new Date());
            tlstCourseSection.setItemCount(child.size());
            tlstCourseSection.setPapperId(tlstCoursePapper.getId());
//            tlstCourseSections.add(tlstCourseSection);
            tlstCourseSectionMapper.insertSelective(tlstCourseSection);
            sectionIndex++;
            //tlst_course_item 生成
            LinkedHashMap<String,List<Map<String, String>>> itemMap = child.stream().filter(a->a.get("item") != null && StringUtils.isNotEmpty(a.get("item"))).collect(Collectors.groupingBy(a->a.get("item"), LinkedHashMap::new, Collectors.toList()));
            Integer itemNameIndex = 1;
            for (Map.Entry<String, List<Map<String, String>>> map2 :itemMap.entrySet()) {
                String key2 =  map2.getKey();
                List<Map<String, String>> child2 =  map2.getValue();
                TlstCourseItem tlstCourseItem = new TlstCourseItem();
                tlstCourseItem.setName("item"+ itemNameIndex);
                tlstCourseItem.setIndex(itemIndex);
                tlstCourseItem.setAudioPath(audioPrefix+tlstCourseSection.getName()+" "+tlstCourseItem.getName()+".mp3");
                tlstCourseItem.setSectionId(tlstCourseSection.getId());
                tlstCourseItem.setType(questionType);
                tlstCourseItem.setQuestionText(child2.get(0).get("文本"));
                tlstCourseItemMapper.insertSelective(tlstCourseItem);
                itemIndex++;
                itemNameIndex++;
                List<TlstCourseOptions> optionList = new ArrayList<>();
                for (Map<String,String> map3 :child2) {
                    TlstCourseOptions tlstCourseOptions = new TlstCourseOptions();
                    tlstCourseOptions.setIndex(optionsIndex);
                    tlstCourseOptions.setSectionId(tlstCourseSection.getId());
                    tlstCourseOptions.setItemId(tlstCourseItem.getId());
                    tlstCourseOptions.setA(map3.get("选项A"));
                    tlstCourseOptions.setB(map3.get("选项B"));
                    tlstCourseOptions.setC(map3.get("选项C"));
                    tlstCourseOptions.setD(map3.get("选项D"));
                    tlstCourseOptions.setRightAnswer(map3.get("正确答案"));
                    tlstCourseOptions.setTips(map3.get("解析"));
                    tlstCourseOptions.setScore(scoreMap.get(tlstCourseItem.getType()));
                    optionList.add(tlstCourseOptions);
                    optionsIndex++;
                }
                tlstCourseOptionsMapper.insertList(optionList);
            }
        }


        //批量插入tlst_course_section
//        tlstCourseSectionMapper.insertList(tlstCourseSections);




        //更新题数
        tlstCoursePapper.setSectionCount(sectionIndex-1);
        tlstCoursePapper.setItemCount(itemIndex-1);
        tlstCoursePapper.setQuestionCount(optionsIndex-1);
        tlstCoursePapperMapper.updateByPrimaryKeySelective(tlstCoursePapper);
        return JSONUtil.toJsonStr(maps);
    }

    private  String customKey(Map<String,Object> map,String name){
        return map.get(name).toString();
    }
}
