package com.rialms.demo

import com.rialms.assessment.item.ItemService
import com.rialms.assessment.test.TestService
import com.rialms.consts.Constants

/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 8/26/12
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
class DemoController {

    ItemService itemService;
    TestService testService;

    def index() { redirect(action: learnBasicAlgegra, params: params) }

    def item = {
        [itemList: itemService.listItemsByFilter(params)]
    }

    def test = {
        [testList: testService.listTestsByFilter(params)]
    }

    def learnBasicAlgegra = {
        redirect(controller: 'test', action: 'play', params: [id:testService.findTestIdByTitle(Constants.learnBasicAlgebraTitle)])
    }
}
