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
                    description: "Question can be scored adaptively over a sequence of attempts. This allows the candidate to alter their answer following feedback or to be posed additional questions based on their current answer"
            ]
            ,
            [
                    name: 'choice',
                    relatesTo: 'item',
                    description: "Presents candidate with a set of choices accepts single choice as response"
            ]
            ,
            [
                    name: 'choice multiple',
                    relatesTo: 'item',
                    description: "Presents candidate with a set of choices accepts multiple choices as response"
            ]
            ,
            [
                    name: 'order',
                    relatesTo: 'item',
                    description: "Presents candidates with a number of choices and allows them to reorder them in correct order"
            ]
            ,
            [
                    name: 'associate',
                    relatesTo: 'item',
                    description: "Presents candidates with a number of choices and allows them to create associations between them"
            ]
            ,
            [
                    name: 'match',
                    relatesTo: 'item',
                    description: "Presents candidates with two sets of choices and allows them to create associations between pairs of choices in the two sets"
            ]
            ,
            [
                    name: 'gap match',
                    relatesTo: 'item',
                    description: "Presents number gaps that the candidate can fill from an associated set of choices"
            ]
            ,
            [
                    name: 'inline choice',
                    relatesTo: 'item',
                    description: "Presents set of choice as dropdown and allows candidate to choose one"
            ]
            ,
            [
                    name: 'text entry',
                    relatesTo: 'item',
                    description: "Obtains a simple piece of text response from the candidate"
            ]
            ,
            [
                    name: 'hottext',
                    relatesTo: 'item',
                    description: "Presents a passage of text with some hot words/phrases highlighted and selectable by the candidate"
            ]
            ,
            [
                    name: 'partial scoring',
                    relatesTo: 'item',
                    description: "Candidate can be awarded partial scores based on number of correct answers"
            ]
            ,
            [
                    name: 'templated',
                    relatesTo: 'item',
                    description: 'Same question can be repeated with different choices (and the correct answer) based on template'
            ]
            ,
            [
                    name: 'hint',
                    relatesTo: 'item',
                    description: 'Presents an option of requesting hint'
            ]
            ,
            [
                    name: 'solution',
                    relatesTo: 'item',
                    description: 'Presents an option of requesting solution'
            ]
            ,
            [
                    name: 'feedback',
                    relatesTo: 'item',
                    description: "Presents feedback conditionally based on candidate's response"
            ]
            ,
            [
                    name: 'math',
                    relatesTo: 'item',
                    description: "Illustrates the inclusion of a mathematical expression marked up with MathML"
            ]
            ,
            [
                    name: 'linear',
                    relatesTo: 'test',
                    description: "Restricts the candidate to attempt each question in turn"
            ]
            ,
            [
                    name: 'nonlinear',
                    relatesTo: 'test',
                    description: "Candidate is free to navigate to any item in the test at any time"
            ]
            ,
            [
                    name: 'individual',
                    relatesTo: 'test',
                    description: "Candidate's responses are submitted item-by-item basis"
            ]
            ,
            [
                    name: 'simultaneous',
                    relatesTo: 'test',
                    description: "Candidate's responses are all submitted together at the end of the testPart"
            ]
            ,
            [
                    name: 'timeout',
                    relatesTo: 'test',
                    description: "Limits the amount of time the candidate is allowed for the test"
            ]
            ,
            [
                    name: 'disabled review',
                    relatesTo: 'test',
                    description: "Candidate cannot go back and review previous questions in individual submission mode"
            ]
            ,
            [
                    name: 'branch',
                    relatesTo: 'test',
                    description: "Number and order of questions presented can be changed depending on the candidate's responses to items presented earlier in the test"
            ]
            ,
            [
                    name: 'disabled skipping',
                    relatesTo: 'test',
                    description: "Candidate cannot skip questions in test."
            ]
            ,
            [
                    name: 'max attempts',
                    relatesTo: 'test',
                    description: "Limits maximum number of attempts allowed to respond to a question for non-adaptive questions"
            ]
            ,
            [
                    name: 'nested',
                    relatesTo: 'test',
                    description: "Test can have sections nested within another section"
            ]
            ,
            [
                    name: 'multiple parts',
                    relatesTo: 'test',
                    description: "Test can have multiple test parts"
            ]

    ]
}