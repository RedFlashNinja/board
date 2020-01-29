package my.painboard.web.controller;

import my.painboard.db.model.Img;
import my.painboard.db.service.implementation.ImgService;
import my.painboard.web.dto.ActionResult;
import my.painboard.web.dto.UIImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/imgs")
public class ImgController {
    @Autowired
    private ImgService imgService;

    @RequestMapping("/list")
    public
    @ResponseBody
    List<UIImage> list() {
        List<UIImage> res = new ArrayList<>();
        List<Img> imgs = imgService.list();
        Collections.sort(imgs, (o1, o2) -> o2.getLevel() - o1.getLevel());
        for (Img img : imgs) {
            res.add(new UIImage(img));
        }
        return res;
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ActionResult modify(@RequestBody UIImage img) {
        if (img.getUuid() == null) {
            imgService.create(null, img.getLevel(), img.getDescription());
        } else {
            imgService.update(img.getUuid(), null, img.getLevel(), img.getDescription());
        }
        return new ActionResult(true, "Seems that ok");
    }

    @RequestMapping("/enable/{uuid}")
    public
    @ResponseBody
    ActionResult enable(@PathVariable String uuid) {
        imgService.enable(uuid, !imgService.getByUuid(uuid).getEnabled());
        return new ActionResult(true, "Seems that ok");
    }

    @RequestMapping("/updateDescripton/{uuid}/{newdesc}")
    public
    @ResponseBody
    ActionResult updateDescripton(@PathVariable String uuid, @PathVariable String newdesc) {
        imgService.updateDescription(uuid, newdesc);
        return new ActionResult(true, "Seems that ok");
    }

    @RequestMapping("/remove/{uuid}")
    public
    @ResponseBody
    ActionResult remove(@PathVariable String uuid) {
        imgService.remove(uuid);
        return new ActionResult(true, "Seems that ok");
    }

    @RequestMapping("/get/{uuid}")
    public
    @ResponseBody
    UIImage get(@PathVariable String uuid) {
        return new UIImage(imgService.getByUuid(uuid));
    }
}
