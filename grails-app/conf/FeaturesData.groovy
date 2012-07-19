/**
 * Created by IntelliJ IDEA.
 * User: relango
 * Date: 7/19/12
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */

data {
    features = [
            [
                    name: 'adaptive',
                    relatesTo: 'item',
                    description: """Allows an item to be scored adaptively over a sequence of attempts.
This allows the candidate to alter their answer following feedback or to be posed additional questions based on their current answer"""
            ]
            ,
            [
                    name: 'choice',
                    relatesTo: 'item',
                    description: """Presents candidate with a set of choices accepts single choice as response"""
            ]
            ,
            [
                    name: 'choice multiple',
                    relatesTo: 'item',
                    description: """Presents candidate with a set of choices accepts multiple choices as response"""
            ]
            ,
            [
                    name: 'order',
                    relatesTo: 'item',
                    description: """Presents candidates with a number of choices and allows them to reorder them in correct order"""
            ]
            ,
            [
                    name: 'associate',
                    relatesTo: 'item',
                    description: """Presents candidates with a number of choices and allows them to create associations between them"""
            ]
            ,
            [
                    name: 'match',
                    relatesTo: 'item',
                    description: """Presents candidates with two sets of choices and allows them to create associations between pairs of choices in the two sets"""
            ]
            ,
            [
                    name: 'gap match',
                    relatesTo: 'item',
                    description: """Presents number gaps that the candidate can fill from an associated set of choices"""
            ]
            ,
            [
                    name: 'inline choice',
                    relatesTo: 'item',
                    description: """Presents set of choice as dropdown and allows candidate to choose one"""
            ]
            ,
            [
                    name: 'text entry',
                    relatesTo: 'item',
                    description: """Obtains a simple piece of text from the candidate"""
            ]
            ,
            [
                    name: 'hottext',
                    relatesTo: 'item',
                    description: """Presents a passage of text with some hot words/phrases highlighted and selectable by the candidate"""
            ]
            ,
            [
                    name: 'partial scoring',
                    relatesTo: 'item',
                    description: """Scores will be computed based on number of correct responses.
so even if candidate's response is not fully correct, he will be awarded partial scores based on number of correct answers"""
            ]
    ]
}