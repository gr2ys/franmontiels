package net.beeapm.demo.service;

import net.beeapm.demo.common.OkHttpUtils;
import net.beeapm.demo.model.RequestVo;
import net.beeapm.demo.model.ResultVo;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author yuanlong.chen
 * @date 2020/01/12
 */
@Service
public class HelloService implements IHelloService {
    private static final Logger log = LoggerFactory.getLogger(HelloService.class);
    private static final int LIMIT = 7;

    @Override
    public ResultVo sayHello(RequestVo vo) {
        hello1();
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    public ResultVo sayGoodbye(RequestVo vo) {
        hello1();
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    public ResultVo welcomeChina(RequestVo vo) {
        hello1();
        return OkHttpUtils.instance().post(vo);
    }

    @Override
    public ResultVo welcomeXiaMen(RequestVo vo) {
        hello1();
        return OkHttpUtils.instance().post(vo);
    }

    public void hello1() {
        try {
            hello2();
        } catch (Exception e) {
            log.error("hello-error", e);
        }
    }

    public void hello2() throws Exception {
        log.debug("hello2");
        hello3();
    }

    public void hello3() throws Exception {
        log.info("hello3");
        hello4();
    }

    public void hello4() throws Exception {
        log.debug("hello4");
        int num = RandomUtils.nextInt(1, 20);
        try {
            Thread.sleep(num);
        } catch (Exception e) {
        }
        if (num < LIMIT) {
            throw new Exception("oh my god");
        }

    }


}
