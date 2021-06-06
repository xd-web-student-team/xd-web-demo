package xd.xdchat.api.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author LSK
 * @date 2021/5/30 - 16:57
 */
@Component
public class DateConverter implements Converter<String, Date> {

  SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  @Override
  public Date convert(String s) {
    Date date = null;
    if (s!=null&&!"".equals(s)){
      try {
       date= simpleDateFormat.parse(s);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return date;
  }
}
